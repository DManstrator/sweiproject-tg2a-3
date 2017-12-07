package ly.muasica.base.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activity")
public class ActivityController {
  
  @Autowired
  private ActivityRepository activityRepository;
  
  
  @GetMapping
  public ArrayList<Activity> listAll() {
      ArrayList<Activity> activities = new ArrayList<>();
      activityRepository.findAll().forEach(activity -> activities.add(activity));
      return activities;
  }

  @GetMapping("{id}")
  public Activity find(@PathVariable Long id) {
      return activityRepository.findOne(id);
  }

  @PostMapping
  public Activity create(@RequestBody Activity input) {
	  System.out.println("create activity");
	  System.out.println(input.getTitle());
	  System.out.println(input.getText());
	  System.out.println(input.getTagsAsString());
	  return activityRepository.save(new Activity(input.getText(), input.getTitle(), input.getTags()));
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
      activityRepository.delete(id);
  }

  @PutMapping("{id}")
  public Activity update(@PathVariable Long id, @RequestBody Activity input) {
      Activity activity = activityRepository.findOne(id);
      if (activity == null) {
          return null;
      } else {
          activity.setText(input.getText());
          activity.setTitle(input.getTitle());
          return activityRepository.save(activity);
      }
  }
  
  @GetMapping("/filter/{name}")
  public List<Activity> filter(@PathVariable String name)  {
      ArrayList<Activity> activities = listAll();

      List<Activity> filtered = activities
              .stream().filter(activity -> activity.getTags().stream()
                      .anyMatch(user -> user.getName().equals(name))
                      )
              .collect(Collectors.toList());
      return filtered;
  }

}
