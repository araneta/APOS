import { Component, ElementRef, ViewChild, OnInit, AfterViewInit } from '@angular/core';
import { Modal } from 'bootstrap';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AccountManagementService } from '../../api/api/accountManagement.service';
import { AccountEntryForm } from '../../api/model/accountEntryForm';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'account-cash-dialog',
  templateUrl: './account-cash-dialog.component.html',
  standalone: true,
  imports: [ReactiveFormsModule],
})
export class AccountCashDialogComponent implements  OnInit,AfterViewInit{
  @ViewChild('modalElement', { static: true }) modalElement!: ElementRef;
  private modalInstance!: Modal;
  akunForm!: FormGroup;

  ngAfterViewInit() {
    this.modalInstance = new Modal(this.modalElement.nativeElement);
  }
  

  constructor(private fb: FormBuilder, private accountManagementService: AccountManagementService,private toastr: ToastrService) {}

  ngOnInit(): void {
        this.akunForm = this.fb.group({
        accountType: [{value:'D',disabled:true}, Validators.required],
        accountCategory: [{ value: 'Asset', disabled: true }],
        parentAccount: [{ value: '1-1100', disabled: true }],
        codePrefix: [{ value: '1', disabled: true }],
        code: ['', Validators.required],
        name: ['', Validators.required],
        isCashBank: [{value:true , disabled: true}],
        });
    }
  open() {
    this.modalInstance.show();
  }

  close() {
    this.modalInstance.hide();
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
        this.accountManagementService.createAccount(accountEntryForm).subscribe({
          next: (response) => {
            console.log('Account created successfully', response);
            this.toastr.success('Account created successfully', 'Success');
            this.close();
          },
          error: (error:any) => {
            console.error('Error creating account:', error.statusText);
            this.toastr.error('Error creating account:'+error.statusText, 'Error');
          }
        });
    }
  }
}
