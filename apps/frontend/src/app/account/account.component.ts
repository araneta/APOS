import { Component, OnInit } from '@angular/core';
import { Account, AccountService } from '../services/account.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { SimpleTableComponent } from '../components/simple-table/simple-table.component';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule, SimpleTableComponent],
  templateUrl: './account.component.html',
  styleUrl: './account.component.scss'
})
export class AccountComponent implements OnInit {
  accounts: Account[] = [];
  selectedAccount?: Account;
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
  
  constructor(private accountService: AccountService, private fb: FormBuilder,private toastr: ToastrService) { 
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
          this.selectAccount(params.data);
        });
        return button;
      }
    }
  ];

  loadData = (state: any) => {
    if (this.accounts.length === 0) {
      this.loadAccounts();
      return;
    }
    
    // Calculate pagination
    const startIndex = (state.currentPage - 1) * state.sizePerPage;
    const endIndex = Math.min(startIndex + state.sizePerPage, this.accounts.length);
    const paginatedData = this.accounts.slice(startIndex, endIndex);
    
    // Update the table data
    this.entities = paginatedData;
    this.totalDataSize = this.accounts.length;
    this.currentPage = state.currentPage;
    this.sizePerPage = state.sizePerPage;
  }

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.loading = true;
    this.accountService.getAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
        this.loading = false;
        // Trigger data load after accounts are loaded
        this.loadData(this.getState());
      },
      error: (err) => {
        this.toastr.error('Failed to load accounts','Error');
        this.loading = false;
      }
    });
  }

  getState() {
    return {
      currentPage: this.currentPage,
      sizePerPage: this.sizePerPage,
      sortName: this.sortName,
      sortOrder: this.sortOrder
    };
  }

  selectAccount(account: Account): void {
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
    if (this.accountForm.invalid) {
      return;
    }
    const accountData = this.accountForm.value;
    if (this.isEditMode && accountData.id) {
      this.accountService.updateAccount(accountData.id, accountData).subscribe({
        next: () => this.loadAccounts(),
        error: () => this.toastr.error('Failed to update account','Error')
      });
    } else {
      this.accountService.createAccount(accountData).subscribe({
        next: () => this.loadAccounts(),
        error: () => this.toastr.error('Failed to create account','Error')
      });
    }
    this.newAccount();
  }

  deleteAccount(id?: number): void {
    if (!id) return;
    if (confirm('Are you sure you want to delete this account?')) {
      this.accountService.deleteAccount(id).subscribe({
        next: () => this.loadAccounts(),
        error: () => this.toastr.error('Failed to delete account','Error')
      });
    }
  }
}
