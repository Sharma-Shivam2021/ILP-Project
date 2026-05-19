import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private readonly apiUrl = '/api/profile';

  constructor(private http: HttpClient) {}

  changePassword(dto: { currentPassword: string; newPassword: string }): Observable<string> {
    return this.http.put(`${this.apiUrl}/change-password`, dto, { responseType: 'text' });
  }

  updateProfile(dto: { fullName: string; contactPhone: string; contactEmail: string }): Observable<any> {
    return this.http.put(`${this.apiUrl}/update`, dto);
  }
}
