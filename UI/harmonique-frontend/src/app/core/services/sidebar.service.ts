import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SidebarService {
  private expandedSource = new BehaviorSubject<boolean>(false);
  expanded$ = this.expandedSource.asObservable();

  setExpanded(state: boolean) {
    this.expandedSource.next(state);
  }
}