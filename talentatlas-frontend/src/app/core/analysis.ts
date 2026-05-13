import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from './environment';

export interface KeywordMatchDto {
  keywordName: string;
  category: string;
  matchedFromSkills: boolean;
  matchedAlias: string;
}

export interface KeywordAnalysisRequest {
  jobDescriptionText: string;
}

export interface KeywordAnalysisResponse {
  totalKeywords: number;
  matchedCount: number;
  missingCount: number;
  matched: KeywordMatchDto[];
  missing: KeywordMatchDto[];
}

@Injectable({
  providedIn: 'root'
})
export class AnalysisService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/analysis/keywords`;

  analyzeKeywords(request: KeywordAnalysisRequest): Observable<KeywordAnalysisResponse> {
    return this.http.post<KeywordAnalysisResponse>(this.apiUrl, request);
  }
}
