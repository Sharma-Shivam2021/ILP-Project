import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateShiftRequestDto, EligiblePeerDto, PeerResponseDto, RequestHistoryResponseDto, ShiftRequestResponseDto, InchargeApprovalDto } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class ShiftRequestService {
  private readonly apiUrl = '/api/shift-requests';

  constructor(private http: HttpClient) {}

  createRequest(dto: CreateShiftRequestDto): Observable<ShiftRequestResponseDto> {
    return this.http.post<ShiftRequestResponseDto>(this.apiUrl, dto);
  }

  peerResponse(dto: PeerResponseDto): Observable<ShiftRequestResponseDto> {
    return this.http.put<ShiftRequestResponseDto>(`${this.apiUrl}/peer-response`, dto);
  }

  getMyRequests(): Observable<ShiftRequestResponseDto[]> {
    return this.http.get<ShiftRequestResponseDto[]>(`${this.apiUrl}/my-requests`);
  }

  getEligiblePeers(requesterShiftId: number): Observable<EligiblePeerDto[]> {
    const params = new HttpParams().set('requesterShiftId', requesterShiftId.toString());
    return this.http.get<EligiblePeerDto[]>(`${this.apiUrl}/eligible-peers`, { params });
  }

  cancelRequest(id: number): Observable<ShiftRequestResponseDto> {
    return this.http.put<ShiftRequestResponseDto>(`${this.apiUrl}/${id}/cancel`, {});
  }

  getRequestHistory(id: number): Observable<RequestHistoryResponseDto[]> {
    return this.http.get<RequestHistoryResponseDto[]>(`${this.apiUrl}/${id}/history`);
  }

  getPendingReview(): Observable<ShiftRequestResponseDto[]> {
    return this.http.get<ShiftRequestResponseDto[]>(`${this.apiUrl}/pending-review`);
  }

  inchargeReview(dto: InchargeApprovalDto): Observable<ShiftRequestResponseDto> {
    return this.http.put<ShiftRequestResponseDto>(`${this.apiUrl}/incharge-review`, dto);
  }
}

