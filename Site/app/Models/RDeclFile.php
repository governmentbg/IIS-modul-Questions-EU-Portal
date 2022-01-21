<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $decl_id
 * @property string     $regN
 * @property int        $person_id
 * @property Date       $decl_date
 * @property int        $decl_type_id
 * @property string     $decl_file
 */
class RDeclFile extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'R_decl_file';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'decl_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'decl_id', 'regN', 'person_id', 'decl_date', 'decl_type_id', 'decl_file'
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
        'decl_id' => 'int', 'regN' => 'string', 'person_id' => 'int', 'decl_date' => 'date', 'decl_type_id' => 'int', 'decl_file' => 'string'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'decl_date'
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
    public function eq_person()
    {
        return $this->belongsTo(RPersons::class, 'person_id');
    }
}
