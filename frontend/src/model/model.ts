export interface Message {
  message: string;
  redirectTo?: string;
}

export interface Card {
  color: string;
  value: string;
}

export interface Player {
  id: string;
  name: string;
}

export interface Team {
  index: number;
  players: Player[];
}

export interface PublicGame {
  id: string;
  creator: string;
  players: number;
}

export interface Scoreboard {
  teamOnePoints: number;
  teamTwoPoints: number;
  finished: boolean;
  winnerTeamIndex: number;
}

export interface Move {
  index: number;
  player: Player;
  card: Card;
}

export interface Round {
  index: number;
  turnOf: Player;
  finished: boolean;
  moves: Move[];
}

export interface Match {
  index: number;
  announcer: Player;
  definitiveAnnouncer: Player;
  pushed: boolean;
  type: string;
}

export interface Game {
  id: string;
  teams: Team[];
  started: boolean;
  match?: Match;
  round?: Round;
  scoreboard?: Scoreboard;
}

export interface MoveHistory {
  player: Player;
  card: Card;
}

export interface RoundHistory {
  winner: Player;
  moves: MoveHistory[];
}

export interface MatchHistory {
  teamOnePoints: number;
  teamTwoPoints: number;
  type: string;
  announcer: Player;
  definitiveAnnouncer: Player;
  rounds: RoundHistory[];
}

export interface GameHistory {
  teamOnePoints: number;
  teamTwoPoints: number;
  winnerTeam: Team;
  teams: Team[];
  matches: MatchHistory[];
}
