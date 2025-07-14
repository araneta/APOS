import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import jsonData from './data.json';

import { ColDef } from 'ag-grid-community';
import { AgGridModule } from 'ag-grid-angular';
import { TreeModule } from 'primeng/tree'; // ✅ Import this
import { TreeNode } from 'primeng/api'; // optional if using type

@Component({
  selector: 'app-tree-grid',
  templateUrl: './tree-grid.component.html',
  styleUrls: ['./tree-grid.component.css'],
  standalone: true,
  imports: [CommonModule, AgGridModule,TreeModule]
})
export class TreeGridComponent implements OnInit {
  public columnDefs: ColDef[] = [
    { field: 'name', cellRenderer: 'agGroupCellRenderer' },
    { field: 'code' },
    { field: 'type' },
    { field: 'category' },
    { field: 'currency' },
  ];
	treeData = [
  {
    label: 'Documents',
    icon: 'pi pi-folder',
    children: [
      { label: 'Resume.pdf', icon: 'pi pi-file' },
      { label: 'CoverLetter.doc', icon: 'pi pi-file' }
    ]
  }
];

  public rowData: any[] = [];

  public autoGroupColumnDef = {
    headerName: 'Account',
    field: 'name',
    cellRendererParams: {
      suppressCount: true,
    }
  };

  ngOnInit() {
    this.rowData = this.buildPathData(jsonData);
  }

  getDataPath = (data: any): string[] => {
    return data.path;
  };

  buildPathData(data: any[]): any[] {
    const map = new Map<number, any>();
    data.forEach(item => map.set(item.id, item));

    const buildPath = (item: any): string[] => {
      const path: string[] = [];
      let current = item;
      while (current) {
        path.unshift(current.name);
        current = current.parent ? map.get(current.parent.id) : null;
      }
      return path;
    };

    return data.map(item => ({
      ...item,
      path: buildPath(item)
    }));
  }
}
