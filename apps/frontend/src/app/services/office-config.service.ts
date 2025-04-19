import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface OfficeConfig {
  id?: number;
  headOfficeCode: string;
  officeName: string;
  timezone: string;
  startMonth: string;
  startYear: number;
  endMonth: string;
}

@Injectable({
  providedIn: 'root'
})
export class OfficeConfigService {
  private apiUrl = `/config`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<OfficeConfig[]> {
    return this.http.get<OfficeConfig[]>(this.apiUrl);
  }

  getById(id: number): Observable<OfficeConfig> {
    return this.http.get<OfficeConfig>(`${this.apiUrl}/${id}`);
  }

  create(config: OfficeConfig): Observable<OfficeConfig> {
    return this.http.post<OfficeConfig>(this.apiUrl, config);
  }

  update(id: number, config: OfficeConfig): Observable<OfficeConfig> {
    return this.http.put<OfficeConfig>(`${this.apiUrl}/${id}`, config);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
