import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { AccountManagementService } from '../api/services/account-management.service';
import { PagingResultAccountDto } from '../api/models/paging-result-account-dto';
import { SearchAccounts$Params } from '../api/fn/account-management/search-accounts';
import { StrictHttpResponse } from '../api/strict-http-response';
import { HttpContext } from '@angular/common/http';
import { ApiConfiguration } from '../api/api-configuration';

export interface Account {
  id?: number;
  code: string;
  name: string;
  parentId?: number;
  description?: string;
  category: string;
  currency: string;
  isCashBank: boolean;
  isActive: boolean;
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AccountService extends AccountManagementService {
  private apiUrl =  '/accounts';

  constructor(
    config: ApiConfiguration,
    http: HttpClient
  ) {
    super(config, http);
  }

  getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.apiUrl);
  }

  getAccount(id: number): Observable<Account> {
    return this.http.get<Account>(`${this.apiUrl}/${id}`);
  }

  createAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(this.apiUrl, account);
  }

  updateAccount(id: number, account: Account): Observable<Account> {
    return this.http.put<Account>(`${this.apiUrl}/${id}`, account);
  }

  deleteAccount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  override searchAccounts(params?: SearchAccounts$Params, context?: HttpContext): Observable<PagingResultAccountDto> {
    return this.searchAccounts$Response(params, context).pipe(
      map((r: StrictHttpResponse<PagingResultAccountDto>): Observable<PagingResultAccountDto> => {
        if (r.body instanceof Blob) {
          return new Observable<PagingResultAccountDto>(observer => {
            const reader = new FileReader();
            reader.onload = () => {
              try {
                const result = JSON.parse(reader.result as string) as PagingResultAccountDto;
                observer.next(result);
                observer.complete();
              } catch (error) {
                observer.error(error);
              }
            };
            reader.onerror = (error) => observer.error(error);
            reader.readAsText(r.body as Blob);
          });
        }
        return new Observable<PagingResultAccountDto>(observer => {
          observer.next(r.body as PagingResultAccountDto);
          observer.complete();
        });
      }),
      mergeMap(obs => obs)
    );
  }
}
