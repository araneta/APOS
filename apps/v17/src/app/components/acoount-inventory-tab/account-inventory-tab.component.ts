import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { AccountDTO } from "../../api/model/accountDTO";
import { ToastrService } from "ngx-toastr";
import { AccountManagementService } from "../../api/api/accountManagement.service";
import { SimpleTableComponent } from "../simple-table/simple-table.component";
import { CommonModule } from '@angular/common';
import { PagingResultAccountDTO } from '../../api/model/pagingResultAccountDTO';

@Component({
  selector: 'app-account-inventory-tab',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SimpleTableComponent],           
    templateUrl: './account-inventory-tab.component.html',           
    //styleUrls: ['./account-Inventory-tab.component.scss']
})
export class AccountInventoryTabComponent implements OnInit {
    inventoryAccounts: AccountDTO[] = [];
    
    
    selectedAccount?: AccountDTO;
    
    isEditMode = false;
    loading = false;
    errorMessage = '';
    totalDataSize = 0;
    currentPage = 1;
    sizePerPage = 10;
    sortName = '';
    sortOrder = 'asc';
    
    constructor(
        private accountManagementService: AccountManagementService,
        private fb: FormBuilder,
        private toastr: ToastrService
    ) {
        
    }
    ngOnInit(): void {
        //this.loadDataInventoryAccounts();
    }
    getColumnDefs = () => [
        { headerName: 'Code', field: 'code' },
        { headerName: 'Name', field: 'name' },
        { headerName: 'Category', field: 'category' },
        { headerName: 'Currency', field: 'currency' },
        { headerName: 'Inventory/Bank', field: 'isInventoryBank' },
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
    public loadDataInventoryAccounts = (state: any) => {
            console.log('Request state:', {
              currentPage: state.currentPage,
              pageSize: state.sizePerPage,
              sortCol: state.sortName,
              sortDir: state.sortOrder,
              searchText: state.searchText,
            });
            this.loading = true;
            
            this.accountManagementService.searchInventoryAccounts(
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
                this.inventoryAccounts = response.data || [];
                this.totalDataSize = response.totalRecords || 0;
                this.currentPage = state.currentPage;
                this.sizePerPage = state.sizePerPage;
                this.loading = false;
                state.successCallback(this.inventoryAccounts, this.totalDataSize, this.currentPage, this.sortName, this.sortOrder);
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


}
