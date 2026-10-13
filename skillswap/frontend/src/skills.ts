export const CATALOG: Record<string,string[]> = {
  Programming:['Java','Python','C','C++','JavaScript','React','Node.js','Spring Boot'],
  'Web Development':['HTML','CSS','React','Angular','UI/UX Design','Figma'],
  Data:['SQL','MySQL','MongoDB','Data Analysis','Machine Learning','Artificial Intelligence'],
  Other:['Graphic Design','Video Editing','Public Speaking','Content Writing','Photography','Digital Marketing'] }
export const ALL_SKILLS = [...new Set(Object.values(CATALOG).flat())].sort()
export const AVATARS = ['🧑‍💻','👩‍💻','👨‍🎓','👩‍🎓','🎨','🚀','🦊','🐼']
