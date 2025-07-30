import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgGridModule } from 'ag-grid-angular';
import { ColDef, GridApi, GridReadyEvent, ICellRendererParams } from 'ag-grid-community';

export interface FlatTreeRow {
  id: string | number;
  parentId: string | number | null;
  label: string;
  [key: string]: any;
}

interface TreeNode {
  id: string | number;
  parentId: string | number | null;
  label: string;
  children?: TreeNode[];
  isExpanded?: boolean;
  level?: number;
  isLeaf?: boolean;
  hasChildren?: boolean;
  [key: string]: any;
}

interface FlatNode extends TreeNode {
  level: number;
  isLeaf: boolean;
  hasChildren: boolean;
}

@Component({
  selector: 'app-tree-cell-renderer',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="tree-cell" [style.padding-left.px]="paddingLeft">
      <span 
        *ngIf="hasChildren" 
        class="tree-toggle"
        (click)="toggle()"
        [class.expanded]="params.data.isExpanded">
        {{ params.data.isExpanded ? '▼' : '▶' }}
      </span>
      <span *ngIf="!hasChildren" class="tree-spacer"></span>
      <span class="tree-label">{{ value }}</span>
    </div>
  `,
  styles: [`
    .tree-cell {
      display: flex;
      align-items: center;
      height: 100%;
    }
    .tree-toggle {
      cursor: pointer;
      margin-right: 4px;
      user-select: none;
      color: #666;
      font-size: 10px;
      width: 12px;
      text-align: center;
    }
    .tree-toggle:hover {
      color: #333;
    }
    .tree-spacer {
      width: 16px;
    }
    .tree-label {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  `]
})
export class TreeCellRenderer {
  params!: ICellRendererParams;
  value!: string;
  hasChildren!: boolean;
  level!: number;
  paddingLeft!: number;

  agInit(params: ICellRendererParams): void {
    this.params = params;
    this.value = params.value;
    this.hasChildren = params.data.hasChildren || false;
    this.level = params.data.level || 0;
    this.paddingLeft = this.level * 20;
  }

  toggle(): void {
    if (this.hasChildren) {
      this.params.context.component.toggleNodeExpansion(this.params.data.id);
    }
  }

  refresh(): boolean {
    return true;
  }
}

@Component({
  selector: 'app-reusable-tree-grid',
  standalone: true,
  imports: [CommonModule, AgGridModule, TreeCellRenderer],
  template: `
    <ag-grid-angular
      class="ag-theme-alpine tree-grid"
      [rowData]="flattenedData"
      [columnDefs]="internalColumnDefs"
      [defaultColDef]="defaultColDef"
      [context]="gridContext"
      [suppressRowClickSelection]="true"
      [headerHeight]="40"
      [rowHeight]="35"
      (gridReady)="onGridReady($event)">
    </ag-grid-angular>
  `,
  styles: [`
    .tree-grid {
      width: 100%;
      height: 100%;
    }
  `]
})
export class ReusableTreeGridComponent implements OnInit {
  @Input() data: FlatTreeRow[] = [];
  @Input() columns: ColDef[] = [];
  @Input() treeColumnHeaderName: string = 'Label';

  private gridApi!: GridApi;
  gridContext = { component: this };
  flattenedData: FlatNode[] = [];
  private treeData: TreeNode[] = [];

  // Always include the tree column as the first column
  get internalColumnDefs(): ColDef[] {
    return [
      {
        headerName: this.treeColumnHeaderName,
        field: 'label',
        cellRenderer: TreeCellRenderer,
        flex: 2,
        suppressSizeToFit: false
      },
      ...this.columns.filter(col => col.field !== 'label')
    ];
  }

  defaultColDef: ColDef = {
    resizable: true,
    sortable: true,
    filter: true
  };

  ngOnInit(): void {
    this.gridContext = { component: this };
    this.buildTreeData();
    this.refreshTree();
  }

  ngOnChanges(): void {
    this.buildTreeData();
    this.refreshTree();
  }

  onGridReady(params: GridReadyEvent): void {
    this.gridApi = params.api;
    this.gridApi.sizeColumnsToFit();
  }

  private buildTreeData(): void {
    // Convert flat data to tree
    const idMap = new Map<string | number, TreeNode>();
    const roots: TreeNode[] = [];
    for (const row of this.data) {
      idMap.set(row.id, { ...row, children: [], isExpanded: false });
    }
    for (const node of idMap.values()) {
      if (node.parentId == null) {
        roots.push(node);
      } else {
        const parent = idMap.get(node.parentId);
        if (parent) {
          parent.children = parent.children || [];
          parent.children.push(node);
        }
      }
    }
    this.treeData = roots;
    this.processTreeData(this.treeData);
  }

  private processTreeData(nodes: TreeNode[], parentId?: string | number, level: number = 0): void {
    nodes.forEach(node => {
      node.level = level;
      node.parentId = parentId ?? null;
      node.isLeaf = !node.children || node.children.length === 0;
      node.hasChildren = !node.isLeaf;
      if (node.hasChildren && node.isExpanded === undefined) {
        node.isExpanded = false;
      }
      if (node.children) {
        this.processTreeData(node.children, node.id, level + 1);
      }
    });
  }

  refreshTree(): void {
    this.flattenedData = this.flattenTree(this.treeData);
    if (this.gridApi) {
      this.gridApi.setGridOption('rowData', this.flattenedData);
    }
  }

  private flattenTree(nodes: TreeNode[]): FlatNode[] {
    const result: FlatNode[] = [];
    const traverse = (nodes: TreeNode[]) => {
      nodes.forEach(node => {
        result.push({
          ...node,
          level: node.level || 0,
          isLeaf: node.isLeaf || false,
          hasChildren: node.hasChildren || false
        });
        if (node.children && node.children.length > 0 && node.isExpanded === true) {
          traverse(node.children);
        }
      });
    };
    traverse(nodes);
    return result;
  }

  toggleNodeExpansion(nodeId: string | number): void {
    const node = this.findNodeById(this.treeData, nodeId);
    if (node && node.hasChildren) {
      node.isExpanded = !node.isExpanded;
      this.refreshTree();
    }
  }

  private findNodeById(nodes: TreeNode[], id: string | number): TreeNode | null {
    for (const node of nodes) {
      if (node.id === id) return node;
      if (node.children) {
        const found = this.findNodeById(node.children, id);
        if (found) return found;
      }
    }
    return null;
  }
} 