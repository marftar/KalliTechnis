package repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

public class CsvRepository<T> {
    private final Path path;
    private final Function<String,T> parser;
    private final Function<T,String> serializer;
    private final Map<Integer,T> data=new LinkedHashMap<>();

    public CsvRepository(String file,Function<String,T> parser,Function<T,String> serializer){
        this.path=Paths.get(file); this.parser=parser; this.serializer=serializer;
    }
    public void load(){
        if(!Files.exists(path)) return;
        try(var lines=Files.lines(path)){
            lines.filter(l->!l.isBlank()).map(parser).forEach(t->data.put(id(t),t));
        }catch(IOException e){ throw new RuntimeException(e); }
    }
    public void save(){
        try{
            Files.createDirectories(Optional.ofNullable(path.getParent()).orElse(Path.of(".")));
            Files.write(path,data.values().stream().map(serializer).toList());
        }catch(IOException e){ throw new RuntimeException(e); }
    }
    public Collection<T> findAll(){ return data.values(); }
    public Optional<T> findById(int id){ return Optional.ofNullable(data.get(id)); }
    public void delete(int id){ data.remove(id); }
    public void save(T t){ data.put(id(t),t); }
    public int nextId(){ return data.keySet().stream().mapToInt(i->i).max().orElse(0)+1; }

    private int id(T t){
        try{ return (int)t.getClass().getMethod("getId").invoke(t); }
        catch(Exception e){ throw new IllegalStateException(e); }
    }
}
