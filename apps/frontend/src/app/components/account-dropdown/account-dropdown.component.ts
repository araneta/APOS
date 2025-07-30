import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-account-dropdown',
  standalone: true,
  imports: [CommonModule], // Import CommonModule here

  templateUrl: './account-dropdown.component.html',
  //styleUrls: ['./account-dropdown.component.css']
})
export class CustomContextMenuComponent {
  @Input() position: { x: number; y: number } = { x: 0, y: 0 };
  @Input() menuItems: { label: string; onClick: (rowData: any) => void }[] = [];
  @Input() rowData: any;

  @Output() close = new EventEmitter<void>();

  handleClick(item: { onClick: (rowData: any) => void }) {
    item.onClick(this.rowData);
    this.close.emit(); // Notify parent to close the menu
  }
}
