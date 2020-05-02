import {ElementSelectionService} from './../../../../../app/element-selection.service';
import {ComponentInspectorService} from './../../../../../app/component-inspector.service';
import { Component } from '@angular/core';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html'
})
export class FooterComponent {constructor(public __elementSelectionService:ElementSelectionService,private __componentInspectorService:ComponentInspectorService) { this.__componentInspectorService.getComp(this); }}
