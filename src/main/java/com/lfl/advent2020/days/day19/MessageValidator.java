package com.lfl.advent2020.days.day19;

import com.lfl.advent2020.LinesConsumer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
public class MessageValidator implements LinesConsumer {

    private final MutableIntObjectMap<Rule> rules = IntObjectMaps.mutable.empty();
    @Getter
    private long count;

    @Override
    public void consume(List<String> lines) {
        int index = 0;
        while (!lines.get(index).isEmpty()) {
            parseRule(lines.get(index));
            index++;
        }
        index++;//pass empty line

        count = IntStream.range(index, lines.size())
                         .mapToObj(lines::get)
                         .filter(this::validate)
                         //.peek(line -> log.info("validated message: {}", line))
                         .count();

        log.info("count = {}", count);
    }

    private boolean validate(String line) {
        Rule firstRule = rules.get(0);
        boolean validate = firstRule.validate(line, IntLists.mutable.of(0));
        return validate && firstRule.newIndexes().contains(line.length());
    }

    private void parseRule(String line) {
        Rule rule;
        if (line.contains("\"")) {
            rule = FinalRule.of(line);
        } else if (line.contains("|")) {
            rule = OrChainedRule.of(line, rules);
        } else {
            rule = ChainedRule.of(line, rules);
        }

        rules.put(rule.name(), rule);
    }
}
