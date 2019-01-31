package protobuf4j.orm.converter;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.Descriptors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepeatedFieldConverter implements IFieldValueConverter {
  private final Map<CharSequence, CharSequence> listLookupMap;
  private final LookupTranslator listValueEscaper;
  private final LookupTranslator listValueUnescaper;
  private final String listSep = ",";
  private final Splitter listSplitter = Splitter.on(listSep);

  //  private final ProtoMessageHelper<?> messageHelper;
  private final Descriptors.FieldDescriptor fieldDescriptor;
  private final IFieldResolver basicTypeFieldResolver;

  public RepeatedFieldConverter(Descriptors.FieldDescriptor fieldDescriptor,
      IFieldResolver basicTypeFieldResolver) {
    // fail fast
    BasicTypeFieldResolver.lookupTransform(fieldDescriptor);
    this.fieldDescriptor = fieldDescriptor;
    this.basicTypeFieldResolver = basicTypeFieldResolver;
    ImmutableMap.Builder<CharSequence, CharSequence> builder = ImmutableMap.builder();
    builder.put(",", "%2c");
    builder.put("%", "%25");
    listLookupMap = builder.build();
    this.listValueEscaper = new LookupTranslator(listLookupMap);
    this.listValueUnescaper = new LookupTranslator(EntityArrays.invert(listLookupMap));
  }

  @Override
  public Class<?> getSqlValueType() {
    return String.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Iterable) {
      StringBuilder sb = new StringBuilder();
      Iterable<?> list = (Iterable<?>) fieldValue;
      for (Object item : list) {
        // 只要有一个元素就有一个分隔符，从而保证元素为string类型时，可以添加空字符串值
        Object v = basicTypeFieldResolver.toSqlValue(fieldDescriptor, item);
        v = listValueEscaper.translate(String.valueOf(v));
        sb.append(v).append(listSep);
      }
      return sb.toString();
    }
    throw new FieldConversionException(
        "fail to convert repeated field, field=" + fieldDescriptor + ", fieldValue=" +
            FieldConversionException.toString(fieldValue));
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return Collections.emptyList();
    }
    if (!(sqlValue instanceof String)) {
      throw new FieldConversionException(
          "fail to parse repeated field, field=" + fieldDescriptor + ", sqlValue=" +
              FieldConversionException.toString(sqlValue));
    }
    String text = (String) sqlValue;
    if (StringUtils.isBlank(text)) {
      return Collections.emptyList();
    }
    // 忽略最后一个分隔符
    if (text.endsWith(listSep)) text = text.substring(0, text.length() - listSep.length());
    List<String> list = listSplitter.splitToList(text);
    return list.stream().map(listValueUnescaper::translate)
        .map(BasicTypeFieldResolver.lookupTransform(fieldDescriptor)).collect(Collectors.toList());
  }
}
