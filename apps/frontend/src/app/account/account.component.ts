import { Component, OnInit } from '@angular/core';
import { AccountManagementService } from '../api/services/account-management.service';
import { AccountDto } from '../api/models/account-dto';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { SimpleTableComponent } from '../components/simple-table/simple-table.component';
import { PagingResultAccountDto } from '../api/models/paging-result-account-dto';
import { AccountService } from '../services/account.service';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule, SimpleTableComponent],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {
  accounts: AccountDto[] = [];
  selectedAccount?: AccountDto;
  accountForm: FormGroup;
  isEditMode = false;
  loading = false;
  errorMessage = '';
  entities: any[] = [];
  totalDataSize = 0;
  currentPage = 1;
  sizePerPage = 10;
  sortName = '';
  sortOrder = 'asc';
  
  constructor(
    private accountService: AccountService,
    private fb: FormBuilder,
    private toastr: ToastrService
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
        });
        return button;
      }
    }
  ];

  loadData = (state: any) => {
    console.log('state', state);
    this.loading = true;
    
    this.accountService.searchAccounts({
      page: state.currentPage,
      pageSize: state.sizePerPage,
      sortCol: state.sortName,
      sortDir: state.sortOrder
    }).subscribe({
      next: (response: PagingResultAccountDto) => {
        console.log('API Response:', response);
        this.entities = response.data || [];
        this.totalDataSize = response.totalRecords || 0;
        this.currentPage = state.currentPage;
        this.sizePerPage = state.sizePerPage;
        this.loading = false;
        state.successCallback(this.entities, this.totalDataSize, this.currentPage, this.sortName, this.sortOrder);
      },
      error: (error) => {
        console.error('Error loading accounts:', error);
        this.errorMessage = 'Failed to load accounts';
        this.loading = false;
      }
    });
  }

  ngOnInit(): void {
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

  selectAccount(account: AccountDto): void {
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
