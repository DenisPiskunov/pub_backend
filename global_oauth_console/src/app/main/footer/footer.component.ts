import {Component, OnInit} from '@angular/core';
import {AppComponent} from '../../app.component';
import {environment} from "../../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {BlockUIService} from "../../service/blockUI.service";
import {VersionService} from "../../service/version.service";
import {forkJoin} from "rxjs";

@Component({
    selector: 'app-footer',
    templateUrl: './footer.component.html'
})
export class FooterComponent implements OnInit {
    serverVersion: string;
    authServerVersion: string;

    clientVersion: string = environment.version

    constructor(public app: AppComponent, private http: HttpClient, private blockUIService: BlockUIService,
                private versionService: VersionService) {
    }

    ngOnInit() {
        this.loadVersions();
    }

    loadVersions() {
        this.blockUIService.block();
        forkJoin(
            [
                this.versionService.getServerVersion(),
                this.versionService.getAuthServerVersion()
            ]
        ).subscribe(([serverVersion, authServerVersion]) => {
            this.serverVersion = serverVersion;
            this.authServerVersion = authServerVersion;
            this.blockUIService.unblock();
        });
    }
}
