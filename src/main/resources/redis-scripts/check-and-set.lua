local currentRaw = redis.call('GET', KEYS[1])
local newDecoded = cjson.decode(ARGV[1])

if currentRaw then
  local currentVersion = cjson.decode(currentRaw).version
  local newVersion = newDecoded.version
  if newVersion > currentVersion then
    redis.call('SET', KEYS[1], ARGV[1])
    return true
  else
    return false
  end
else
  redis.call('SET', KEYS[1], ARGV[1])
  return true
end

