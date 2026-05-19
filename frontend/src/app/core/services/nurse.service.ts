import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NurseResponseDto, NurseShiftResponseDto } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class NurseService {
  private readonly apiUrl = '/api/nurses';

  constructor(private http: HttpClient) {}

  getMyProfile(): Observable<NurseResponseDto> {
    return this.http.get<NurseResponseDto>(`${this.apiUrl}/me`);
  }

  getMyShifts(): Observable<NurseShiftResponseDto[]> {
    return this.http.get<NurseShiftResponseDto[]>(`${this.apiUrl}/me/shifts`);
  }

  getNursesByDepartment(departmentId: number): Observable<NurseResponseDto[]> {
    return this.http.get<NurseResponseDto[]>(`${this.apiUrl}/department/${departmentId}`);
  }
}
