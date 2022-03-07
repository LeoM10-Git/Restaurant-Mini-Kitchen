import { Session } from "./session.model";

export interface BookingSession {
  date: string;
  sessions: Session[];
}
