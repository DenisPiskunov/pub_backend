import {Component, OnDestroy, OnInit} from '@angular/core';
import {SecurityContextHolder} from '../../mt-security/security-context-holder';
import {MenuBuilder} from './menu-builder';
import {Subscription} from 'rxjs';
import {MainComponent} from '../main.component';
import {LocalizationService} from '../../mt-support/localization.service';

@Component({
    selector: 'app-menu',
    templateUrl: './menu.component.html',
})
export class MenuComponent implements OnInit, OnDestroy {

    model: any[];
    private readonly menuBuilder: MenuBuilder;
    private lngChangeSubscription: Subscription;

    constructor(public appMain: MainComponent, localizationService: LocalizationService, securityContextHolder: SecurityContextHolder) {
        this.menuBuilder = new MenuBuilder(securityContextHolder, localizationService);
        this.lngChangeSubscription = localizationService.onLangChangeEvent().subscribe(() => {
            this.initMenu();
        });
    }

    ngOnInit() {
        this.initMenu();
    }

    initMenu() {
        this.model = this.menuBuilder.build();
    }

    ngOnDestroy(): void {
        this.lngChangeSubscription.unsubscribe();
    }
}
