import { Component } from '@angular/core';
import { ColDef } from 'ag-grid-community';
import { CommonModule } from '@angular/common';
import { AgGridModule } from 'ag-grid-angular';

@Component({
  selector: 'app-simple-tree',
  standalone: true,
  imports: [CommonModule, AgGridModule],
  templateUrl: './simple-tree.component.html',
  styleUrls: ['./simple-tree.component.css']
})
export class SimpleTreeComponent {
  // Example tree data: each node has a 'path' array for its position in the tree
  public rowData = [
    { name: 'Root 1', value: 10, path: ['Root 1'] },
    { name: 'Child 1.1', value: 5, path: ['Root 1', 'Child 1.1'] },
    { name: 'Child 1.2', value: 7, path: ['Root 1', 'Child 1.2'] },
    { name: 'Root 2', value: 20, path: ['Root 2'] },
    { name: 'Child 2.1', value: 8, path: ['Root 2', 'Child 2.1'] }
  ];

  public columnDefs: ColDef[] = [
    {
      headerName: 'Name',
      field: 'name',
      cellRenderer: 'agGroupCellRenderer', // Enables tree expand/collapse
    },
    {
      headerName: 'Value',
      field: 'value',
    }
  ];

  // Required for ag-Grid tree data
  getDataPath = (data: any) => data.path;

  onGridReady(params: any) {
    params.api.sizeColumnsToFit();
  }
}
