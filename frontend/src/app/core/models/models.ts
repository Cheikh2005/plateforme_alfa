export interface User {
  id: number;
  username: string;
  email: string;
  nom: string;
  prenom: string;
  role: string;
  serie: string;
  dateInscription?: string;
}

export interface LoginResponse {
  token: string;
  type: string;
  id: number;
  username: string;
  email: string;
  nom: string;
  prenom: string;
  role: string;
  serie: string;
}

export interface Matiere {
  id: number;
  nom: string;
  code: string;
  description: string;
  couleur: string;
  icone: string;
}

export interface Cours {
  id: number;
  matiere: Matiere;
  titre: string;
  chapitre: string;
  numeroChapitre: number;
  contenu: string;
  resume: string;
  type: 'COURS' | 'RESUME' | 'FICHE';
  niveau: string;
  dureeLecture: number;
  dateCreation: string;
}

export interface OptionReponse {
  id: number;
  texte: string;
  estCorrecte?: boolean;
  ordre: number;
}

export interface Question {
  id: number;
  enonce: string;
  explication: string;
  ordre: number;
  points: number;
  options: OptionReponse[];
}

export interface Exercice {
  id: number;
  matiere: Matiere;
  titre: string;
  enonce: string;
  correction: string;
  difficulte: 'FACILE' | 'MOYEN' | 'DIFFICILE';
  type: 'EXERCICE' | 'QCM' | 'ANNALE';
  anneeBac: number;
  dureeMinutes: number;
  pointsTotal: number;
  questions: Question[];
  dateCreation: string;
}

export interface UserProgress {
  id: number;
  cours: Cours;
  pourcentage: number;
  complete: boolean;
  dateDebut: string;
  dateCompletion: string;
}

export interface UserScore {
  id: number;
  exercice: Exercice;
  score: number;
  scoreMax: number;
  tempsPasseMinutes: number;
  datePassage: string;
}

export interface Planning {
  id: number;
  matiere?: Matiere;
  titre: string;
  description: string;
  dateDebut: string;
  dateFin: string;
  heureDebut: string;
  heureFin: string;
  couleur: string;
  complete: boolean;
  rappel: boolean;
  dateCreation: string;
}

export interface ChatMessage {
  id: number;
  message: string;
  reponse: string;
  matiere?: Matiere;
  dateMessage: string;
}

export interface DashboardStats {
  userId: number;
  nom: string;
  prenom: string;
  serie: string;
  coursComplets: number;
  totalCours: number;
  progressionMoyenne: number;
  exercicesFaits: number;
  scoreMoyen: number;
  tasksDone: number;
  totalTasks: number;
  totalExercices: number;
  recentScores: UserScore[];
  upcomingPlannings: Planning[];
}

export interface SubmitResult {
  score: number;
  scoreMax: number;
  pourcentage: number;
  correctAnswers: number;
  totalQuestions: number;
  resultats: { [key: number]: boolean };
  mention: string;
}
