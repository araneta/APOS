import { Component, ElementRef, ViewChild, OnInit, AfterViewInit, EventEmitter, Output } from '@angular/core';
import { Modal } from 'bootstrap';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AccountManagementService } from '../../api/api/accountManagement.service';
import { AccountEntryForm } from '../../api/model/accountEntryForm';
import { ToastrService } from 'ngx-toastr';
import { CommonModule, NgIf, NgClass } from '@angular/common';

@Component({
  selector: 'account-inventory-dialog',
  templateUrl: './account-inventory-dialog.component.html',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf,
    NgClass,
    CommonModule,],
})
export class AccountInventoryDialogComponent implements OnInit, AfterViewInit {
  @ViewChild('modalElement', { static: true }) modalElement!: ElementRef;
  @Output() onClose = new EventEmitter<boolean>();
  private modalInstance!: Modal;
  formAlert: { type: 'success' | 'error', message: string } | null = null;

  akunForm!: FormGroup;
  id: number | null = null;

  ngAfterViewInit() {
    this.modalInstance = new Modal(this.modalElement.nativeElement);
  }


  constructor(private fb: FormBuilder, private accountManagementService: AccountManagementService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.akunForm = this.fb.group({
      accountType: [{ value: 'D', disabled: true }, Validators.required],
      accountCategory: [{ value: 'Asset', disabled: true }],
      parentAccount: [{ value: '1-2000', disabled: true }],
      codePrefix: [{ value: '1', disabled: true }],
      code: ['', Validators.required],
      name: ['', Validators.required],
      isCashBank: [{ value: false, disabled: true }],
    });
  }
  open() {
    this.formAlert = null;
    this.modalInstance.show();
  }

  close() {
    this.formAlert = null;
    this.modalInstance.hide();
    this.id = null; // Reset id when closing    
    this.akunForm.patchValue({
      code: '',
      name: '',
    });
    this.onClose.emit(true); // Or false if no update needed
  }
  edit(id: number, code: string, name: string) {
    this.id = id; // Set the id for the account being edited
    this.akunForm.patchValue({
      code: code,
      name: name,

    });
    this.modalInstance.show();
  }
  submitForm(): void {
    if (this.akunForm.valid) {
      const payload = this.akunForm.getRawValue(); // includes disabled values
      console.log('Form submitted:', payload);
      var accountEntryForm: AccountEntryForm;
      accountEntryForm = {
        code: payload.code,
        name: payload.name,
        isCashBank: payload.isCashBank,
        accountCategory: payload.accountCategory,
        accountType: payload.accountType,
        parentAccount: payload.parentAccount,
        codePrefix: payload.codePrefix,
      };
      if (this.id) {
        this.accountManagementService.updateAccount(this.id, accountEntryForm).subscribe({
          next: (response) => {
            this.formAlert = { type: 'success', message: 'Account updated successfully' };
            setTimeout(() => this.close(), 1500); // Close after showing alert
          },
          error: (error: any) => {
            this.formAlert = { type: 'error', message: 'Error updating account: ' + error.error.message };
          }
        });
      } else {
        this.accountManagementService.createAccount(accountEntryForm).subscribe({
          next: (response) => {
            this.formAlert = { type: 'success', message: 'Account created successfully' };
            setTimeout(() => this.close(), 1500);
          },
          error: (error: any) => {
            this.formAlert = { type: 'error', message: 'Error creating account: ' + error.statusText };
          }
        });
      }


    }
  }
}
