import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface OfficeConfig {
  id?: number;
  headOfficeCode: string;
  officeName: string;
  timezone: string;
  startMonth: number;
  startYear: number;
  endMonth: number;
}

@Injectable({
  providedIn: 'root'
})
export class OfficeConfigService {
  private apiUrl = `/config`;

  constructor(private http: HttpClient) {}

  get(): Observable<OfficeConfig> {
    return this.http.get<OfficeConfig>(`${this.apiUrl}`);
  }

  save(config: OfficeConfig): Observable<OfficeConfig> {
    return this.http.post<OfficeConfig>(this.apiUrl, config);
  }

}
