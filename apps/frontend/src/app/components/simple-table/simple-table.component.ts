import {
  Component,
  Input,
  Output,
  EventEmitter,
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
  @Output() getRows = new EventEmitter<any>();
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
        width: 90,
        menuTabs: [],
        sortable: false,
        filter: false,
        suppressMovable: true
      },
      ...colDefs
    ];
  }

  onGridReady(params: any) {
    console.log('onGridReady',params);  
    this.gridApi = params.api;
    this.gridColumnApi = params.columnApi;
    this.gridApi.sizeColumnsToFit();
    //params.api.setHeaderHeight(32);
    this.getRows.emit(this.getState());
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
      searchText: this.searchText,
      successCallback: this.successCallback.bind(this),
    };
  }

  successCallback(
    entities: any[],
    totalDataSize: number,
    currentPage: number,
    sortName: string,
    sortOrder: string
  ) {
    console.log('successCallback', entities, totalDataSize, currentPage, sortName, sortOrder);
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
      this.currentPage = 1; // Reset to first page on search
      console.log('onSearchChange', value);
      this.getRows.emit(this.getState());
    }, 1000);
  }

  onSizePerPageList(size: number) {
    this.sizePerPage = size;
    this.currentPage = 1;
    this.getRows.emit(this.getState());
  }

  onSortChange(sortName: string, sortOrder: string) {
    this.sortName = sortName;
    this.sortOrder = sortOrder;
    this.getRows.emit(this.getState());
  }

  handleSortChanged(params: any) {
    console.log('handleSortChanged', params);
    if (params.source === 'uiColumnSorted') {            
      const sortedColumns = params.columns.filter((col: { sort: string }) => col.sort);
      if (sortedColumns.length > 0) {
        //console.log('Sorted Columns:', sortedColumns);
        const { colId, sort } = sortedColumns[0];
        console.log('Column ID:', colId, 'Sort Order:', sort);
        this.onSortChange(colId, sort);
      }
    }
  }

  handlePageChange(page: number) {
    this.currentPage = page;
    this.getRows.emit(this.getState());
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

  get totalPages(): number {
    return Math.ceil(this.totalDataSize / this.sizePerPage) || 1;
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i + 1);
  }

  get visiblePages(): number[] {
    const total = this.totalPages;
    const current = this.currentPage;
    const delta = 2; // Number of pages to show before and after current
    let start = Math.max(1, current - delta);
    let end = Math.min(total, current + delta);

    // Adjust if at the start or end
    if (current <= delta) {
      end = Math.min(total, 1 + 2 * delta);
    }
    if (current + delta > total) {
      start = Math.max(1, total - 2 * delta);
    }

    const pages = [];
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    return pages;
  }
}
