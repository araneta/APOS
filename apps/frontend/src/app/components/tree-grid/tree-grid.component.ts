import { Component, Input, OnInit, OnChanges, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AgGridModule } from 'ag-grid-angular';
import { ColDef, GridApi, GridReadyEvent } from 'ag-grid-community';

import { FlatTreeRow, ActionButton, TreeNode, FlatNode } from './interfaces/tree-grid.interface';
import { TreeCellRenderer } from './cell-renderers/tree-cell-renderer.component';
import { ActionsCellRenderer } from './cell-renderers/actions-cell-renderer.component';
import { TreeDataService } from './services/tree-data.service';
import { CustomButtonComponent } from './CustomButtonComponent';

@Component({
  selector: 'tree-grid',
  standalone: true,
  imports: [CommonModule, AgGridModule, TreeCellRenderer, ActionsCellRenderer],
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
  styleUrls: ['./tree-grid.component.css']
})
export class TreeGridComponent implements OnInit, OnChanges {
  @Input() data: FlatTreeRow[] = [];
  @Input() columns: ColDef[] = [];
  @Input() treeColumnHeaderName: string = 'Label';
  @Input() showActions: boolean = true;
  @Input() actionButtons: ActionButton[] = [];
  @Input() actionsColumnWidth: number = 150;
  @Input() actionsColumnHeaderName: string = 'Actions';
  
  @Output() actionButtonClicked = new EventEmitter<{action: string, data: any}>();

  private gridApi!: GridApi;
  gridContext = { component: this };
  flattenedData: FlatNode[] = [];
  private treeData: TreeNode[] = [];

  constructor(private treeDataService: TreeDataService) {}

  // Build column definitions including tree column and optional actions column
  get internalColumnDefs(): ColDef[] {
    const columnDefs: ColDef[] = [
      {
        headerName: this.treeColumnHeaderName,
        field: 'label',
        cellRenderer: TreeCellRenderer,
        flex: 2,
        suppressSizeToFit: false
      },
      ...this.columns.filter(col => col.field !== 'label')
    ];

    // Add actions column if enabled and has buttons
    if (this.showActions && this.actionButtons.length > 0) {
      
      columnDefs.push({
  colId: "actions",
  headerName: "Actions",
  cellRenderer: ActionsCellRenderer, // ✅ works for standalone components
  flex: 2,
  pinned: 'right',
  suppressSizeToFit: false,
  sortable: false,
  filter: false,
  resizable: false
});

      /*
      columnDefs.push({
        colId: "actions",
        headerName: "Actions",
        cellRenderer: CustomButtonComponent,
      });*/
    }

    return columnDefs;
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

  // Action handler called from ActionsCellRenderer
  onActionButtonClick(action: string, rowData: any): void {
    console.log('Action clicked:', action, 'for:', rowData);
    this.actionButtonClicked.emit({ action, data: rowData });
  }

  private buildTreeData(): void {
    this.treeData = this.treeDataService.buildTreeData(this.data);
  }

  refreshTree(): void {
    this.flattenedData = this.treeDataService.flattenTree(this.treeData);
    if (this.gridApi) {
      this.gridApi.setGridOption('rowData', this.flattenedData);
    }
  }

  toggleNodeExpansion(nodeId: string | number): void {
    const node = this.treeDataService.findNodeById(this.treeData, nodeId);
    if (node && node.hasChildren) {
      node.isExpanded = !node.isExpanded;
      this.refreshTree();
    }
  }

}
