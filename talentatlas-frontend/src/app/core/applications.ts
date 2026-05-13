import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from './environment';

export interface ApplicationRecord {
  id: number;
  companyName: string;
  positionTitle: string;
  jobPostingUrl: string | null;
  primaryContactName: string | null;
  primaryContactPhone: string | null;
  primaryContactEmail: string | null;
  status: string;
  appliedAt: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
  statusUpdatedAt?: string | null;
  daysInCurrentStatus?: number;
  needsAttention?: boolean;
  attentionLevel?: string;
  attentionMessage?: string;
  notes: string | null;
}


export interface CreateApplicationRequest {
  companyName: string;
  positionTitle: string;
  jobPostingUrl: string | null;
  primaryContactName: string | null;
  primaryContactPhone: string | null;
  primaryContactEmail: string | null;
  status: string;
  appliedAt: string | null;
  notes: string | null;
}

@Injectable({
  providedIn: 'root',
})
export class ApplicationsService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/applications`;

  getApplications(): Observable<ApplicationRecord[]> {
    return this.http.get<ApplicationRecord[]>(this.apiUrl);
  }

  getApplicationById(id: number): Observable<ApplicationRecord> {
    return this.http.get<ApplicationRecord>(`${this.apiUrl}/${id}`);
  }

  createApplication(
    request: CreateApplicationRequest
  ): Observable<ApplicationRecord> {
    return this.http.post<ApplicationRecord>(this.apiUrl, request);
  }

  updateApplication(
    id: number,
    request: CreateApplicationRequest
  ): Observable<ApplicationRecord> {
    return this.http.put<ApplicationRecord>(`${this.apiUrl}/${id}`, request);
  }

  deleteApplication(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}