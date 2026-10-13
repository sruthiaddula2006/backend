export interface User { id:number; name:string; email:string; college:string; location:string; bio:string; avatar:string; skillsKnown:string[]; skillsWanted:string[] }
export interface Match { user:User; score:number; canTeach:string[]; canLearn:string[]; reason:string }
export interface SwapRequest { id:number; senderId:number; senderName:string; receiverId:number; receiverName:string; skillsOffered:string[]; skillsRequested:string[]; message:string; status:'PENDING'|'ACCEPTED'|'REJECTED'; createdAt:string }
export interface Exchange { id:number; partner:User; youTeach:string[]; youLearn:string[]; status:'ACTIVE'|'COMPLETED'; createdAt:string }
export interface Message { id:number; senderId:number; receiverId:number; text:string; timestamp:string }
export interface Notif { id:number; type:string; message:string; read:boolean; createdAt:string }
