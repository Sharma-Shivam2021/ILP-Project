import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DepartmentStaffingDto, NurseResponseDto, ShiftRequestResponseDto } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class DutyOfficerService {
  private readonly apiUrl = '/api/duty-officer';

  constructor(private http: HttpClient) {}

  getStaffingReport(date: string): Observable<DepartmentStaffingDto> {
    const params = new HttpParams().set('date', date);
    return this.http.get<DepartmentStaffingDto>(`${this.apiUrl}/staffing-report`, { params });
  }

  getDepartmentNurses(): Observable<NurseResponseDto[]> {
    return this.http.get<NurseResponseDto[]>(`${this.apiUrl}/nurses`);
  }

  getDepartmentShiftRequests(): Observable<ShiftRequestResponseDto[]> {
    return this.http.get<ShiftRequestResponseDto[]>(`${this.apiUrl}/shift-requests`);
  }
}
