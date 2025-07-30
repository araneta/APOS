import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagingResultAccountDTO } from '../api/model/pagingResultAccountDTO';
import { Configuration } from '../api/configuration';
import { AccountManagementService } from '../api/api/accountManagement.service';
import { HttpContext } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private accountManagementService: AccountManagementService;

  constructor(
    http: HttpClient,
    config: Configuration
  ) {
    this.accountManagementService = new AccountManagementService(http, '', config);
  }

  searchAccounts(
    filter?: string,
    page?: number,
    pageSize?: number,
    sortCol?: string,
    sortDir?: string
  ): Observable<PagingResultAccountDTO> {
    return this.accountManagementService.searchAccounts(
      filter,
      page,
      pageSize,
      sortCol,
      sortDir,
      'body' as const,
      false,
      { httpHeaderAccept: 'application/json' }
    );
  }
}
