
import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ICellRendererParams } from 'ag-grid-community';
import { ActionButton } from '../interfaces/tree-grid.interface';
import { ICellRendererAngularComp } from 'ag-grid-angular';

@Component({
  selector: 'app-actions-cell-renderer',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="actions-cell">
      <ng-container *ngFor="let button of visibleButtons; trackBy: trackByAction">
        <button 
          [class]="getButtonClass(button)"
          [disabled]="isButtonDisabled(button)"
          [title]="button.tooltip || ''"
          (click)="onButtonClick(button)"
          type="button">
          <span *ngIf="button.icon" [innerHTML]="button.icon" class="button-icon"></span>
          <span class="button-label">{{ button.label }}</span>
        </button>
      </ng-container>
    </div>
  `,
  styleUrls: ['./actions-cell-renderer.component.css']
})
export class ActionsCellRenderer implements ICellRendererAngularComp {  // ✅ Make sure this export exists
  params!: ICellRendererParams;
  actionButtons: ActionButton[] = [];
  visibleButtons: ActionButton[] = [];

  constructor(private cdr: ChangeDetectorRef) {}

  agInit(params: ICellRendererParams): void {
    //console.log('🔧 ActionsCellRenderer agInit called');
    
    this.params = params;
    
    if (params.context?.component?.actionButtons) {
      this.actionButtons = params.context.component.actionButtons;
      //console.log('✅ Action buttons found:', this.actionButtons);
    } else {
      //console.warn('❌ No action buttons found in context');
      this.actionButtons = [];
    }
    
    this.updateVisibleButtons();
    this.cdr.detectChanges();
  }

  refresh(): boolean {
    //console.log('🔄 ActionsCellRenderer refresh called');
    this.updateVisibleButtons();
    this.cdr.detectChanges();
    return true;
  }

  private updateVisibleButtons(): void {
    this.visibleButtons = this.actionButtons.filter(button => {
      if (button.visible && typeof button.visible === 'function') {
        const isVisible = button.visible(this.params.data);
        //console.log(`👁️ Button "${button.label}" visibility:`, isVisible);
        return isVisible;
      }
      return true;
    });
    
    //console.log('✨ Visible buttons after filtering:', this.visibleButtons);
  }

  getButtonClass(button: ActionButton): string {
    const baseClass = 'btn';
    const sizeClass = 'btn-sm';
    const styleClass = button.cssClass || 'btn-primary';
    return `${baseClass} ${sizeClass} ${styleClass}`;
  }

  isButtonDisabled(button: ActionButton): boolean {
    if (button.disabled && typeof button.disabled === 'function') {
      return button.disabled(this.params.data);
    }
    return false;
  }

  onButtonClick(button: ActionButton): void {
    console.log('🖱️ Button clicked:', button.label, button.action);
    
    if (this.params.context?.component?.onActionButtonClick) {
      this.params.context.component.onActionButtonClick(button.action, this.params.data);
    } else {
      console.error('❌ onActionButtonClick method not found in context component');
    }
  }

  trackByAction(index: number, button: ActionButton): string {
    return button.action;
  }
}
