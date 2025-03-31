import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { ToastrService } from 'ngx-toastr';
import { tap } from 'rxjs/operators';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  // Add other product properties as needed
}

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = environment.apiUrl + '/products';

  constructor(
    private http: HttpClient,
    private toastr: ToastrService
  ) {}

  private handleError(error: any) {
    if (error.status === 0) {
      this.toastr.error('Server is offline or unreachable. Please try again later.', 'Connection Error');
    } else if (error.status === 401) {
      this.toastr.error('Your session has expired. Please login again.', 'Session Expired');
    } else if (error.status === 403) {
      this.toastr.error('You do not have permission to perform this action.', 'Access Denied');
    } else {
      this.toastr.error(error.message || 'An error occurred while fetching products.', 'Error');
    }
    return throwError(() => error);
  }

  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError.bind(this))
    );
  }

  createProduct(product: Omit<Product, 'id'>): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product).pipe(
      tap(() => {
        this.toastr.success('Product created successfully!', 'Success');
      }),
      catchError(this.handleError.bind(this))
    );
  }

  updateProduct(id: number, product: Partial<Product>): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product).pipe(
      tap(() => {
        this.toastr.success('Product updated successfully!', 'Success');
      }),
      catchError(this.handleError.bind(this))
    );
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => {
        this.toastr.success('Product deleted successfully!', 'Success');
      }),
      catchError(this.handleError.bind(this))
    );
  }
}
