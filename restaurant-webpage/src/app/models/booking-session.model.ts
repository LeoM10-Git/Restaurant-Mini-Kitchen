import { Session } from "./session.model";

export interface BookingSession {
  date: Date;
  sessions: Session[];
}
