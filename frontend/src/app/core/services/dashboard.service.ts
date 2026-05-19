import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DutyOfficerDashboardDto, InchargeDashboardDto, NurseDashboardDto } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private readonly apiUrl = '/api/dashboard';

  constructor(private http: HttpClient) {}

  getNurseDashboard(): Observable<NurseDashboardDto> {
    return this.http.get<NurseDashboardDto>(`${this.apiUrl}/nurse`);
  }

  getInchargeDashboard(): Observable<InchargeDashboardDto> {
    return this.http.get<InchargeDashboardDto>(`${this.apiUrl}/incharge`);
  }

  getDutyOfficerDashboard(): Observable<DutyOfficerDashboardDto> {
    return this.http.get<DutyOfficerDashboardDto>(`${this.apiUrl}/duty-officer`);
  }
}
