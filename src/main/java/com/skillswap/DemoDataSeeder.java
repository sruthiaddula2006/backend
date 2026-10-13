package com.skillswap;
import com.skillswap.model.User; import com.skillswap.model.repo.UserRepository; import org.springframework.boot.CommandLineRunner; import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.crypto.password.PasswordEncoder; import java.util.*;
/** Inserts demo users when SEED_DEMO=true and the table is empty. All demo passwords: demo123. Keep SEED_DEMO=false in production. */
@Configuration @ConditionalOnProperty(name="app.seed-demo",havingValue="true")
class DemoDataSeeder {
  private static User u(PasswordEncoder enc,String name,String email,String college,String loc,String bio,String avatar,List<String> known,List<String> wanted){
    User x=new User(); x.setName(name); x.setEmail(email); x.setPasswordHash(enc.encode("demo123")); x.setCollege(college); x.setLocation(loc); x.setBio(bio); x.setAvatar(avatar); x.getSkillsKnown().addAll(known); x.getSkillsWanted().addAll(wanted); return x; }
  @Bean CommandLineRunner seed(UserRepository repo,PasswordEncoder e){ return a->{ if(repo.count()>0) return; repo.saveAll(List.of(
    u(e,"Harshitha (Demo)","demo@skillswap.com","Demo University","Hyderabad","Exploring full-stack development.","👩‍💻",List.of("Java","Python","HTML"),List.of("React","Machine Learning")),
    u(e,"Rahul Verma","rahul@demo.com","JNTU","Hyderabad","Backend enthusiast.","🧑‍💻",List.of("Java","HTML","CSS"),List.of("Python")),
    u(e,"Ananya Sharma","ananya@demo.com","IIIT Hyderabad","Hyderabad","CS student into ML.","👩‍🎓",List.of("Python","Machine Learning","SQL"),List.of("Java","Spring Boot")),
    u(e,"Arjun Reddy","arjun@demo.com","VIT","Chennai","Frontend developer.","🚀",List.of("React","JavaScript"),List.of("UI/UX Design")),
    u(e,"Priya Nair","priya@demo.com","NIFT","Bengaluru","Designer who loves clean UI.","🎨",List.of("UI/UX Design","Figma"),List.of("React")),
    u(e,"Karthik Rao","karthik@demo.com","BITS Pilani","Hyderabad","Full-stack tinkerer.","👨‍🎓",List.of("React","Node.js"),List.of("Java","Spring Boot")),
    u(e,"Sneha Iyer","sneha@demo.com","NIT Trichy","Chennai","Data analyst.","🦊",List.of("Machine Learning","Data Analysis","SQL"),List.of("HTML","Python")),
    u(e,"Meera Das","meera@demo.com","Christ University","Bengaluru","Visual storyteller.","🐼",List.of("Photography","Video Editing"),List.of("Public Speaking")),
    u(e,"Vikram Singh","vikram@demo.com","Delhi University","Delhi","Marketer and speaker.","🧑‍💻",List.of("Public Speaking","Digital Marketing"),List.of("Photography")))); }; }
}
