import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ICellRendererParams } from 'ag-grid-community';

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
  styleUrls: ['./tree-cell-renderer.component.css']
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