import {
  Component,
  Input,
  ViewChild,
  ElementRef,
  AfterViewInit,
  OnDestroy
} from '@angular/core';
import { GridOptions, GridApi, ColDef, ModuleRegistry, AllCommunityModule } from 'ag-grid-community';
import { CommonModule } from '@angular/common';
import { AgGridModule } from 'ag-grid-angular';
import { CustomContextMenuComponent } from '../custom-context-menu/custom-context-menu.component';

ModuleRegistry.registerModules([AllCommunityModule]);

@Component({
  selector: 'app-simple-table',
  standalone: true,
  imports: [CommonModule, AgGridModule, CustomContextMenuComponent],

  templateUrl: './simple-table.component.html',
  styleUrls: ['./simple-table.component.css']
})
export class SimpleTableComponent implements AfterViewInit, OnDestroy {
  @Input() getColumnDefs!: () => ColDef[];
  @Input() onGetRows!: (state: any) => void;
  @Input() sortName: string = 'ID';
  @Input() sortOrder: string = 'asc';
  @Input() showNo: boolean = false;
  @Input() entities: any[] = [];

  // Add Math property to make it accessible in template
  Math = Math;

  @ViewChild('agGrid') agGrid: any;
  @ViewChild('tableContainer') tableContainer!: ElementRef;

  gridApi!: GridApi;
  gridColumnApi: any;

  columnDefs: ColDef[] = [];
  totalDataSize = 0;
  sizePerPage = 10;
  currentPage = 1;
  searchText = '';
  contextMenu: any = null;
  private searchTimeout: any;

  ngAfterViewInit() {
    this.columnDefs = this.getColumnDefs();
    if (this.showNo) this.validateColDefs(this.columnDefs);
  }

  ngOnDestroy() {
    window.removeEventListener('resize', this.handleResize);
  }

  validateColDefs(colDefs: ColDef[]) {
    this.columnDefs = [
      {
        headerName: 'No',
        valueGetter: this.rowNumberGetter.bind(this),
        width: 60,
        menuTabs: [],
        sortable: false,
        filter: false,
        suppressMovable: true
      },
      ...colDefs
    ];
  }

  onGridReady(params: any) {
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    this.gridApi.sizeColumnsToFit();
    params.api.setHeaderHeight(32);
    this.onGetRows(this.getState());
  }

  getState() {
    return {
      columnDefs: this.columnDefs,
      entities: this.entities,
      totalDataSize: this.totalDataSize,
      sizePerPage: this.sizePerPage,
      currentPage: this.currentPage,
      sortName: this.sortName,
      sortOrder: this.sortOrder,
      searchText: this.searchText
    };
  }

  successCallback(
    entities: any[],
    totalDataSize: number,
    currentPage: number,
    sortName: string,
    sortOrder: string
  ) {
    this.entities = entities;
    this.totalDataSize = totalDataSize;
    this.currentPage = currentPage;
    this.sortName = sortName;
    this.sortOrder = sortOrder;
  }

  onSearchChange(event: any) {
    const value = event.target.value;
    if (this.searchTimeout) clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => {
      this.searchText = value;
      this.onGetRows(this.getState());
    }, 1000);
  }

  onSizePerPageList(size: number) {
    this.sizePerPage = size;
    this.currentPage = 1;
    this.onGetRows(this.getState());
  }

  onSortChange(sortName: string, sortOrder: string) {
    this.sortName = sortName;
    this.sortOrder = sortOrder;
    this.onGetRows(this.getState());
  }

  handleSortChanged(params: any) {
    if (params.source === 'uiColumnSorted') {
      const sortedColumns = params.columnApi.getColumnState().filter((col: { sort: string }) => col.sort);
      if (sortedColumns.length > 0) {
        const { colId, sort } = sortedColumns[0];
        this.onSortChange(colId, sort);
      }
    }
  }

  handlePageChange(page: number) {
    this.currentPage = page;
    this.onGetRows(this.getState());
  }

  rowNumberGetter(params: any) {
    const zeroBasedPage = this.currentPage - 1;
    return params.node.rowIndex + 1 + zeroBasedPage * this.sizePerPage;
  }

  showContextMenu(event: MouseEvent, rowData: any, menuItems: any[]) {
    const cell = (event.target as HTMLElement).getBoundingClientRect();
    const rect = this.tableContainer.nativeElement.getBoundingClientRect();
    this.contextMenu = {
      position: {
        x: cell.left - rect.left,
        y: cell.top - rect.top + cell.height
      },
      rowData,
      menuItems
    };
  }

  handleResize = () => {
    if (this.gridApi) {
      this.gridApi.sizeColumnsToFit();
    }
  };
}
