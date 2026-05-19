import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AssignShiftDto, NurseShiftResponseDto, ShiftResponseDto, CreateShiftDto } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ShiftService {
  private readonly apiUrl = '/api/shifts';

  constructor(private http: HttpClient) {}

  assignShift(dto: AssignShiftDto): Observable<NurseShiftResponseDto> {
    return this.http.post<NurseShiftResponseDto>(`${this.apiUrl}/assign`, dto);
  }

  getDepartmentSchedule(departmentId: number, date: string): Observable<NurseShiftResponseDto[]> {
    const params = new HttpParams().set('date', date);
    return this.http.get<NurseShiftResponseDto[]>(`${this.apiUrl}/department/${departmentId}/schedule`, { params });
  }

  getAllShifts(): Observable<ShiftResponseDto[]> {
    return this.http.get<ShiftResponseDto[]>(this.apiUrl);
  }

  createShift(dto: CreateShiftDto): Observable<ShiftResponseDto> {
    return this.http.post<ShiftResponseDto>(this.apiUrl, dto);
  }
}
