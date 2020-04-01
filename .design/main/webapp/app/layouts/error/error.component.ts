import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-error',
  templateUrl: './error.component.html'
})
export class ErrorComponent implements OnInit {
  errorMessage: string;
  error403: boolean;
  error404: boolean;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
private route: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.route.data.subscribe(routeData => {
      if (routeData.error403) {
        this.error403 = routeData.error403;
      }
      if (routeData.error404) {
        this.error404 = routeData.error404;
      }
      if (routeData.errorMessage) {
        this.errorMessage = routeData.errorMessage;
      }
    });
  }
}
