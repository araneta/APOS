import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { CommonModule } from '@angular/common'; // Import CommonModule

@Component({
    standalone: true,
  selector: 'app-tree-cell-renderer',
  imports: [CommonModule], // Import CommonModule to use *ngIf and other directives
  template: `
    <span *ngIf="hasChildren" (click)="toggle()" style="cursor: pointer; padding-right: 4px;">
      {{ data.expanded ? '▼' : '▶' }}
    </span>
    <span [style.paddingLeft.px]="data.level * 15">
      {{ data.name }}
    </span>
  `,
})
export class TreeCellRendererComponent implements ICellRendererAngularComp {
  public data: any;
  public hasChildren: boolean = false;
  private params: any;

  agInit(params: any): void {
    this.params = params;
    this.data = params.data;
    const allRows = params.context.componentParent.allData;
    this.hasChildren = allRows.some((d: any) => d.parentId === this.data.id);
  }

  refresh(): boolean {
    return false;
  }

  toggle() {
    this.data.expanded = !this.data.expanded;
    this.params.context.componentParent.rebuildVisibleRows();
  }
}
