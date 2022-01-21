<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $Lib_id
 * @property int        $LibT_id
 * @property string     $Lib_N
 * @property Date       $Lib_date
 * @property string     $Lib_file
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class LibBulletin extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'Lib_Bulletin';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'Lib_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'Lib_id', 'LibT_id', 'Lib_N', 'Lib_date', 'Lib_file', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'Lib_id' => 'int', 'LibT_id' => 'int', 'Lib_N' => 'string', 'Lib_date' => 'date', 'Lib_file' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'Lib_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_type()
    {
        return $this->belongsTo(LibBulletinType::class, 'LibT_id');
    }
}
