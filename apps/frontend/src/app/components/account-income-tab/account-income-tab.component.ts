import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { AccountDTO } from "../../api/model/accountDTO";
import { ToastrService } from "ngx-toastr";
import { ColDef } from 'ag-grid-community';
import { AccountManagementService } from "../../api/api/accountManagement.service";
import { CommonModule } from '@angular/common';
import { PagingResultAccountDTO } from '../../api/model/pagingResultAccountDTO';
import { FlatTreeRow, TreeGridComponent, ActionButton } from '../tree-grid';


@Component({
  selector: 'app-account-income-tab',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TreeGridComponent],
  templateUrl: './account-income-tab.component.html',
  //styleUrls: ['./account-Income-tab.component.scss']
})
export class AccountIncomeTabComponent implements OnInit {
  incomeAccounts: AccountDTO[] = [];
  selectedAccount?: AccountDTO;

  isEditMode = false;
  loading = false;
  errorMessage = '';
  totalDataSize = 0;
  currentPage = 1;
  sizePerPage = 10;
  sortName = '';
  sortOrder = 'asc';

  treeRows: FlatTreeRow[] = [
  ];

  treeColumns: ColDef[] = [
    { headerName: 'Name', field: 'name' },
    { headerName: 'Type', field: 'type' }
  ];
  showActionsColumn: boolean = true;
  actionButtons: ActionButton[] = [
  {
    label: 'Edit',
    action: 'edit',
    cssClass: 'btn-primary',
    icon: '<i class="fa fa-edit"></i>',
    visible: () => true,
    disabled: () => false
  },
  {
    label: 'Delete',
    action: 'delete',
    cssClass: 'btn-danger',
    icon: '<i class="fa fa-trash"></i>',
    visible: () => true,
    disabled: () => false
  }
];

  constructor(
    private accountManagementService: AccountManagementService,
    private fb: FormBuilder,
    private toastr: ToastrService
  ) {

  }

  convertAccountsToTreeRows(data: AccountDTO[]): FlatTreeRow[] {
    return data.map(account => ({
      id: account.id ?? 0,
      parentId: account.parent?.id ?? null,
      label: account.code ?? '',
      name: account.name ?? '',
      type: account.type ?? '-', // or account.category or other property
    }));
  }

  ngOnInit(): void {
    console.log('AccountIncomeTabComponent initialized');
    this.loadDataIncomeAccounts({
      currentPage: this.currentPage,
      pageSize: this.sizePerPage,
      sortCol: this.sortName,
      sortDir: this.sortOrder,
      searchText: '',
    });
  }

  public loadDataIncomeAccounts = (state: any) => {
    console.log('Request state:', {
      currentPage: state.currentPage,
      pageSize: state.sizePerPage,
      sortCol: state.sortName,
      sortDir: state.sortOrder,
      searchText: state.searchText,
    });
    this.loading = true;

    this.accountManagementService.searchIncomeAccounts(
      state.searchText, // filter
      state.currentPage,
      state.sizePerPage,
      state.sortName,
      state.sortOrder,
      'body' as const,
      false,
      { httpHeaderAccept: 'application/json' }
    ).subscribe({
      next: (response: PagingResultAccountDTO) => {
        console.log('API Response:', response);
        this.incomeAccounts = response.data || [];
        this.totalDataSize = response.totalRecords || 0;
        this.currentPage = state.currentPage;
        this.sizePerPage = state.sizePerPage;
        this.loading = false;
        this.treeRows = this.convertAccountsToTreeRows(this.incomeAccounts);
      },
      error: (error) => {
        console.error('Error details:', {
          status: error.status,
          statusText: error.statusText,
          error: error.error,
          message: error.message
        });
        this.errorMessage = 'Failed to load cashAccounts';
        this.loading = false;
      }
    });
  }

  onAction(event: any) {
    alert(`Action: ${event.action}, Data: ${JSON.stringify(event.data)}`);
  }
}
