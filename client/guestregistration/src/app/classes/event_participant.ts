import { Agent } from "./agent";
import { Card } from "./card";

export class EventParticipant{
    id: string
    agent: Agent;
    card: Card;
    eventId: string;
}