import { Component, OnInit } from '@angular/core';
import { Account, AccountService } from '../services/account.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
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

  ngOnInit(): void {
    // Initialization logic here
    this.loadAccounts();
  }

  loadAccounts(): void {
    this.loading = true;
    this.accountService.getAccounts().subscribe({
      next: (data) => {
        this.accounts = data;
        this.loading = false;
      },
      error: (err) => {
        this.toastr.error('Failed to load accounts','Error');
        this.loading = false;
      }
    });
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
