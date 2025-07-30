import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { AccountDTO } from "../../api/model/accountDTO";
import { ToastrService } from "ngx-toastr";
import { AccountManagementService } from "../../api/api/accountManagement.service";
import { SimpleTableComponent } from "../simple-table/simple-table.component";
import { CommonModule } from '@angular/common';
import { PagingResultAccountDTO } from '../../api/model/pagingResultAccountDTO';
import { AccountCashDialogComponent } from "../account-cash-dialog/account-cash-dialog.component";
import { LoadingService } from '../../loading.service';

@Component({
  selector: 'app-account-cash-tab',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,
    SimpleTableComponent, AccountCashDialogComponent],
  templateUrl: './account-cash-tab.component.html',
  //styleUrls: ['./account-cash-tab.component.scss']
})
export class AccountCashTabComponent implements OnInit {
  cashAccounts: AccountDTO[] = [];
  selectedAccount?: AccountDTO;

  isEditMode = false;
  loading = false;
  errorMessage = '';
  totalDataSize = 0;
  currentPage = 1;
  sizePerPage = 10;
  sortName = '';
  sortOrder = 'asc';
  @ViewChild('modalRef') modalComponent!: AccountCashDialogComponent;
  @ViewChild('grid') gridComponent!: SimpleTableComponent;


  constructor(
    private accountManagementService: AccountManagementService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private loadingService: LoadingService,
  ) {

  }
  ngOnInit(): void {
    //this.loadDataCashAccounts();

  }
  getColumnDefs = () => [
    { headerName: 'Code', field: 'code' },
    { headerName: 'Name', field: 'name' },
    { headerName: 'Type', field: 'type' },
    //{ headerName: 'Currency', field: 'currency' },
    //{ headerName: 'Cash/Bank', field: 'cashBank' },
    //{ headerName: 'Active', field: 'active' },
    {
      headerName: 'Actions',
      cellRenderer: (params: any) => {
        const container = document.createElement('div');

        // Edit button
        const editButton = document.createElement('button');
        editButton.innerText = 'Edit';
        editButton.classList.add('btn', 'btn-sm', 'btn-primary', 'me-2');
        editButton.addEventListener('click', () => {
          console.log('edit', params.data);
          this.modalComponent.edit(params.data.id, params.data.code, params.data.name);
        });

        // Delete button
        const deleteButton = document.createElement('button');
        deleteButton.innerText = 'Delete';
        deleteButton.classList.add('btn', 'btn-sm', 'btn-danger');
        deleteButton.addEventListener('click', () => {
          console.log('delete', params.data);
          this.deleteAccount(params.data.id); // Make sure this function exists in your component
        });

        container.appendChild(editButton);
        container.appendChild(deleteButton);

        return container;
      }
    }
  ];


  public loadDataCashAccounts = (state: any) => {
    console.log('Request state:', {
      currentPage: state.currentPage,
      pageSize: state.sizePerPage,
      sortCol: state.sortName,
      sortDir: state.sortOrder,
      searchText: state.searchText,
    });
    this.loading = true;

    this.accountManagementService.searchCashAccounts(
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

  showModal() {
    this.modalComponent.open();
  }

  handleModalClose(updated: boolean) {
    if (updated) {
      this.loadingService.show();
      // Reload data if the modal was closed with an update
      this.gridComponent.refresh();

      // after operation is done
      this.loadingService.hideAfter();

    }
  }

  deleteAccount(id: number) {
    if (confirm('Are you sure you want to delete this account?')) {
      this.loadingService.show();

      this.accountManagementService.deleteAccount(id).subscribe({
        next: () => {
          this.toastr.success('Account deleted successfully');
          this.gridComponent.refresh(); // Refresh the table after deletion
          this.loadingService.hideAfter();
        },
        error: (error: any) => {
          console.error('Delete error:', error);
          this.toastr.error('Failed to delete account');
          this.loadingService.hideAfter();
        }
      });
    }
  }

}
