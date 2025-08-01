import { Component, OnInit } from '@angular/core';
import { AccountDTO } from '../api/model/accountDTO';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { SimpleTableComponent } from '../components/simple-table/simple-table.component';
import { PagingResultAccountDTO } from '../api/model/pagingResultAccountDTO';
import { AccountManagementService } from '../api/api/accountManagement.service';
import {AccountCashTabComponent} from '../components/account-cash-tab/account-cash-tab.component';
import { AccountInventoryTabComponent } from '../components/acoount-inventory-tab/account-inventory-tab.component';
import { AccountIncomeTabComponent } from '../components/account-income-tab/account-income-tab.component';
import { AccountExpenseTabComponent } from '../components/account-expense-tab/account-expense-tab.component';
import {TreeGridComponent} from '../components/tree-grid/tree-grid.component';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule, SimpleTableComponent, 
    AccountCashTabComponent, AccountInventoryTabComponent, 
    AccountIncomeTabComponent,AccountExpenseTabComponent,
    TreeGridComponent
    ],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {
  
  cashAccounts: AccountDTO[] = [];
  inventoryAccounts: AccountDTO[] = [];
  incomeAccounts: AccountDTO[] = [];
  expenseAccounts: AccountDTO[] = [];

  selectedAccount?: AccountDTO;
  accountForm: FormGroup;
  isEditMode = false;
  loading = false;
  errorMessage = '';
  //entities: any[] = [];
  totalDataSize = 0;
  currentPage = 1;
  sizePerPage = 10;
  sortName = '';
  sortOrder = 'asc';
  
  constructor(
    private accountManagementService: AccountManagementService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private titleService: Title
  ) { 
    this.accountForm = this.fb.group({
      id: [null],
      code: ['', Validators.required],
      name: ['', Validators.required],
      parentId: [null],
      description: [''],
      category: ['', Validators.required],
      currency: ['', Validators.required],
      isCashBank: [false],
      isActive: [true]
    });
  }
  tableData: any[] = [];
  totalSize: number = 0;

  getColumnDefs = () => [
    { headerName: 'Code', field: 'code' },
    { headerName: 'Name', field: 'name' },
    { headerName: 'Category', field: 'category' },
    { headerName: 'Currency', field: 'currency' },
    { headerName: 'Cash/Bank', field: 'isCashBank' },
    { headerName: 'Active', field: 'isActive' },
    {
      headerName: 'Actions',
      cellRenderer: (params: any) => {
        const button = document.createElement('button');
        button.innerText = 'Edit';
        button.classList.add('btn', 'btn-sm', 'btn-primary');
        button.addEventListener('click', () => {
          //this.selectAccount(params.data);
          console.log('edit',params.data);
        });
        return button;
      }
    }
  ];

  loadDataIncomeAccounts = (state: any) => {
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
        this.cashAccounts = response.data || [];
        this.totalDataSize = response.totalRecords || 0;
        this.currentPage = state.currentPage;
        this.sizePerPage = state.sizePerPage;
        this.loading = false;
        state.successCallback(this.cashAccounts, this.totalDataSize, this.currentPage, this.sortName, this.sortOrder);
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

  loadDataExpenseAccounts = (state: any) => {
    console.log('Request state:', {
      currentPage: state.currentPage,
      pageSize: state.sizePerPage,
      sortCol: state.sortName,
      sortDir: state.sortOrder,
      searchText: state.searchText,
    });
    this.loading = true;
    
    this.accountManagementService.searchExpenseAccounts(
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
        this.cashAccounts = response.data || [];
        this.totalDataSize = response.totalRecords || 0;
        this.currentPage = state.currentPage;
        this.sizePerPage = state.sizePerPage;
        this.loading = false;
        state.successCallback(this.expenseAccounts, this.totalDataSize, this.currentPage, this.sortName, this.sortOrder);
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

  ngOnInit(): void {
    this.titleService.setTitle('Accounts Management');

    // Data will be loaded when the grid is ready
  }

  getState() {
    return {
      currentPage: this.currentPage,
      sizePerPage: this.sizePerPage,
      sortName: this.sortName,
      sortOrder: this.sortOrder
    };
  }

  selectAccount(account: AccountDTO): void {
    this.isEditMode = true;
    this.selectedAccount = account;
    this.accountForm.patchValue(account);
  }

  newAccount(): void {
    this.isEditMode = false;
    this.selectedAccount = undefined;
    this.accountForm.reset({
      isCashBank: false,
      isActive: true
    });
  }

  saveAccount(): void {
    console.log(this.accountForm.value);
  }

}
