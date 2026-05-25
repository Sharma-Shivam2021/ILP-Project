import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdminUserResponseDto, RegisterRequestDto, AuditLog, DepartmentResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);
  private readonly apiUrl = '/api/admin';

  // User Management
  getAllUsers(): Observable<AdminUserResponseDto[]> {
    return this.http.get<AdminUserResponseDto[]>(`${this.apiUrl}/users`);
  }

  createUser(userData: RegisterRequestDto): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/users`, userData);
  }

  updateUser(userId: number, userData: RegisterRequestDto): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/users/${userId}`, userData);
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/users/${userId}`);
  }

  resetPassword(userId: number, newPassword: String): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/users/${userId}/reset-password`, { newPassword });
  }

  // Department Management
  getDepartments(): Observable<DepartmentResponse[]> {
    return this.http.get<DepartmentResponse[]>(`${this.apiUrl}/departments`);
  }

  createDepartment(deptData: { name: string; location: string }): Observable<DepartmentResponse> {
    return this.http.post<DepartmentResponse>(`${this.apiUrl}/departments`, deptData);
  }

  updateDepartment(deptId: number, deptData: { name: string; location: string }): Observable<DepartmentResponse> {
    return this.http.put<DepartmentResponse>(`${this.apiUrl}/departments/${deptId}`, deptData);
  }

  deleteDepartment(deptId: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/departments/${deptId}`);
  }

  // Audit Logs
  getAuditLogs(): Observable<AuditLog[]> {
    return this.http.get<AuditLog[]>(`${this.apiUrl}/audit-logs`);
  }

  // Stats
  getStats(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/stats`);
  }
}
