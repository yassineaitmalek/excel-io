package com.test.services.excel.template;

// import java.io.FileOutputStream;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.nio.file.Paths;

// import org.jxls.common.Context;
// import org.jxls.util.JxlsHelper;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.stereotype.Service;

// import com.test.utility.FileUtility;
// import com.mgcc.rpsiel.infrastructure.config.properties.IOPaths;
// import com.mgcc.rpsiel.infrastructure.exception.config.ApiException;
// import com.mgcc.rpsiel.persistence.models.local.input.Person;

// import io.vavr.control.Try;
// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
public class ExcelTemplate {

  // private final IOPaths ioPaths;

  // public InputStream loadResource(String path) {
  // return Try.of(() -> path)
  // .map(ClassPathResource::new)
  // .mapTry(ClassPathResource::getInputStream)
  // .getOrElseThrow(() -> new ApiException("error loading the resource " +
  // path));

  // }

  // public void createEtat(Person person) {
  // FileUtility
  // .createFolder(Paths.get(ioPaths.getOutput(), person.getFullName()));

  // try (OutputStream outStream = new FileOutputStream(
  // Paths.get(ioPaths.getOutput(), person.getFullName(), "ES_" +
  // person.getFullName() + ".xls").toString())) {

  // Context context = new Context();
  // context.putVar("adress", person.getAdress());
  // context.putVar("cin", person.getCin());
  // context.putVar("rib", person.getRib());
  // context.putVar("fullName", person.getFullName());

  // JxlsHelper.getInstance().processTemplate(loadResource("templates/Etat.xls"),
  // outStream, context);

  // } catch (Exception e) {
  // throw new ApiException("error creating the resource ", e);
  // }

  // }

}
