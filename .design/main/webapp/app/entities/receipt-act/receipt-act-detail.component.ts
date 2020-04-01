import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReceiptAct } from 'app/shared/model/receipt-act.model';

@Component({
  selector: 'jhi-receipt-act-detail',
  templateUrl: './receipt-act-detail.component.html'
})
export class ReceiptActDetailComponent implements OnInit {
  receiptAct: IReceiptAct;

  constructor(public __elementSelectionService:ElementSelectionService, private __componentInspectorService:ComponentInspectorService,
protected activatedRoute: ActivatedRoute) {this.__componentInspectorService.getComp(this);
}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ receiptAct }) => {
      this.receiptAct = receiptAct;
    });
  }

  previousState() {
    window.history.back();
  }
}
