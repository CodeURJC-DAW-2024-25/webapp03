import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ChartDataItemDTO } from '../dtos/chartDataItem.dto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChartService {
  private apiUrl = '/api/courses/chartdata';

  constructor(private http: HttpClient) {}

  getChartData(): Observable<ChartDataItemDTO[]> {
    return this.http.get<ChartDataItemDTO[]>(this.apiUrl);
  }
}
