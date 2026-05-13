import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from './environment';

export interface SkillResponse {
  id: number;
  name: string;
  category: string;
  proficiency: number;
}

export interface SkillRequest {
  name: string;
  category: string;
  proficiency: number;
}

@Injectable({
  providedIn: 'root',
})
export class SkillService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/skills`;

  getSkills(): Observable<SkillResponse[]> {
    return this.http.get<SkillResponse[]>(this.apiUrl);
  }

  getSkillById(id: number): Observable<SkillResponse> {
  return this.http.get<SkillResponse>(`${this.apiUrl}/${id}`);
}

  createSkill(request: SkillRequest): Observable<SkillResponse> {
    return this.http.post<SkillResponse>(this.apiUrl, request);
  }

  updateSkill(id: number, request: SkillRequest): Observable<SkillResponse> {
  return this.http.put<SkillResponse>(`${this.apiUrl}/${id}`, request);
  }

  deleteSkill(id: number): Observable<void> {
  return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
