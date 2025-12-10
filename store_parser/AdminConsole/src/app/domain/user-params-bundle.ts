import {UserDetails} from './user-details';

export interface UserParamsBundle {
    username?: string;
    oldUsername?: string;
    password?: string;
    user?: UserDetails;
}
